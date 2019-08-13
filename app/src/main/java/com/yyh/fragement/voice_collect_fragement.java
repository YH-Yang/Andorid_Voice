package com.yyh.fragement;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.iflytek.cloud.record.PcmRecorder;
import com.iflytek.cloud.util.VerifierUtil;
import com.yyh.IdentifyGroup.GroupManagerActivity;
import com.yyh.R;

import static android.support.constraint.Constraints.TAG;

public class voice_collect_fragement extends Fragment  implements View.OnClickListener {
    private static final int PWD_TYPE_TEXT = 1;
    private static final int PWD_TYPE_FREE = 2;
    private static final int PWD_TYPE_NUM = 3;
    // 当前声纹密码类型，1、2、3分别为文本、自由说和数字密码
    private int mPwdType = PWD_TYPE_FREE;
    // 声纹识别对象
    private SpeakerVerifier mVerifier;
    // 身份验证对象
    private IdentityVerifier mIdVerifier;
    // 声纹AuthId，用户在云平台的身份标识，也是声纹模型的标识
    // 请使用英文字母或者字母和数字的组合，勿使用中文字符
    private String authId = "";
    private String name;
    private boolean isMale;       //个人资料
    private String role;
    private EditText mResultEditText;
    private TextView mAuthIdTextView;
    private TextView mShowPwdTextView;
    private TextView mShowMsgTextView;
    private TextView mShowRegFbkTextView;
    private TextView mRecordTimeTextView;
    private Toast mToast;
    private EditText mAuthidEditText;
    private AlertDialog  mTextPwdSelectDialog;

    // 文本声纹密码
    private String mTextPwd = "";


    // 是否可以录音
    private boolean mCanStartRecord = false;
    // 是否可以录音
    private boolean isStartWork = false;
    // 录音采样率
    private final int SAMPLE_RATE = 16000;
    // pcm录音机
    private PcmRecorder mPcmRecorder;
    // 进度对话框
    private ProgressDialog mProDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.isvdemo,container,false);

        //初始化身份识别对象
        mIdVerifier = IdentityVerifier.createVerifier(getContext(), null);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //SpeechUtility.createUtility(getContext(), SpeechConstant.APPID +"=5cff96ac");
        SpeechUtility.createUtility(getContext(), SpeechConstant.APPID +"=5d4d7462");
        initui();
        authId="yyh";
        mAuthidEditText.setText(authId);

        //初始化speakerverifier
        mVerifier = SpeakerVerifier.createVerifier(getActivity(), new InitListener() {


            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    showTip("引擎初始化成功");
                } else {
                    showTip("引擎初始化失败，错误码：" + errorCode);
                }
            }
        });
    }


    //初始化组件
   public void initui(){
        mResultEditText = (EditText)getView().findViewById(R.id.edt_result);
        mAuthidEditText = (EditText) getView().findViewById(R.id.set_authId);
       mShowPwdTextView = (TextView)getView().findViewById(R.id.showPwd);
       mShowMsgTextView = (TextView) getView().findViewById(R.id.showMsg);
       mShowRegFbkTextView = (TextView)getView().findViewById(R.id.showRegFbk);
       mRecordTimeTextView = (TextView)getView().findViewById(R.id.recordTime);

       getView(). findViewById(R.id.isv_setup).setOnClickListener(this);
        getView().findViewById(R.id.isv_getpassword).setOnClickListener(this);
        getView().findViewById(R.id.isv_search).setOnClickListener(this);
        getView().findViewById(R.id.isv_delete).setOnClickListener(this);
        getView().findViewById(R.id.isv_identity).setOnClickListener(this);
        getView().findViewById(R.id.isv_group).setOnClickListener(this);

        //密码选择 首先设置为文本
       mPwdType=PWD_TYPE_TEXT;

       mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
       mToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);


    }
    //初始化TextView与文本密码
    private void initTextView(){
        mTextPwd = null;
        mResultEditText.setText("");
        mShowPwdTextView.setText("");
        mShowMsgTextView.setText("");
        mShowRegFbkTextView.setText("");
        mRecordTimeTextView.setText("");
    }

    /*
    执行模型
     */
private void performModelOperation(String  operation, SpeechListener listener){
    //清空参数
    mVerifier.setParameter(SpeechConstant.PARAMS,null);
    //设置密码类型
    mVerifier.setParameter(SpeechConstant.ISV_PWDT,mPwdType+"");
    if(mPwdType==PWD_TYPE_TEXT){
        //文本密码删除需要传入密码
        if(TextUtils.isEmpty(mTextPwd)){
           showTip("请获得密码后进行操作");
           return ;
        }
        mVerifier.setParameter(SpeechConstant.ISV_PWD,mTextPwd);
    }else if(mPwdType==PWD_TYPE_NUM){

    }else if(mPwdType==PWD_TYPE_FREE){

    }

    //设置auth_id 不能设置为空
    mVerifier.sendRequest(operation,authId,listener);

}

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        //获得密码
        case R.id.isv_getpassword:
            //获得密码之前的注册或验证过程
            mVerifier.cancel();
            initTextView();
            //清空参数
            mVerifier.setParameter(SpeechConstant.PARAMS,null);
            mVerifier.setParameter(SpeechConstant.ISV_PWDT,""+mPwdType);
            // 本地的监听参数
            if (mPwdType!=PWD_TYPE_FREE)
            mVerifier.getPasswordList(mPwdListenter);
            break;
            //寻找
        case R.id.isv_search:
            performModelOperation("que",mModelOperationListener);
            break;
            //删除
        case R.id.isv_delete:
            performModelOperation("del",mModelOperationListener);
            break;
        //注册
        case R.id.isv_setup:
            mVerifier.setParameter(SpeechConstant.PARAMS,null);
            mVerifier.setParameter(SpeechConstant.ISE_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getAbsolutePath()+"/isv/test.pcm");
            // 对于某些麦克风非常灵敏的机器，如nexus、samsung i9300等，建议加上以下设置对录音进行消噪处理
            mVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
            //如果为文本密码
            if(mPwdType==PWD_TYPE_TEXT){
                if (TextUtils.isEmpty(mTextPwd)) {
                    showTip("请获取密码后进行操作");
                    return;
                }
            mVerifier.setParameter(SpeechConstant.ISV_PWD,mTextPwd);
            mShowPwdTextView.setText("请读出"+mTextPwd);
            mShowMsgTextView.setText("训练 第" + 1 + "遍，剩余4遍");
            }
            //如果是数字密码
            else if(mPwdType==PWD_TYPE_NUM){

            }
            //如果是自由说
            else if(mPwdType==PWD_TYPE_FREE){
             //这里插一句嘴，自由说的注册参数之次数 设置为“1” 音质的的设置“8000”
                mVerifier.setParameter(SpeechConstant.ISV_RGN,"1");
                mVerifier.setParameter(SpeechConstant.SAMPLE_RATE,"8000");
            }
            //设置auth_id 不能为空
            mVerifier.setParameter(SpeechConstant.AUTH_ID,authId);
            //设置业务类型为注册
            mVerifier.setParameter(SpeechConstant.ISV_SST,"train");
            //设置声纹密码类型
            mVerifier.setParameter(SpeechConstant.ISV_PWDT,""+mPwdType);
            //开始注册
            mVerifier.startListening(mRegisterListener);
            break;
            //识别
        case R.id.isv_identity:
            //清空提示信息
            ((TextView) getView().findViewById(R.id.showMsg)).setText("");
            // 清空参数
            mVerifier.setParameter(SpeechConstant.PARAMS, null);
            mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/isv/verify.pcm");
            mVerifier = SpeakerVerifier.getVerifier();
            mVerifier.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
            //设置业务类型为验证
            mVerifier.setParameter(SpeechConstant.ISV_SST,"verify");
            //如果是文本密码
            if (mPwdType == PWD_TYPE_TEXT) {
                // 文本密码注册需要传入密码
                if (TextUtils.isEmpty(mTextPwd)) {
                    showTip("请获取密码后进行操作");
                    return;
                }
                mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
                ((TextView) getView().findViewById(R.id.showPwd)).setText("请读出："
                        + mTextPwd);
            }
            //如果是数值密码
            else if(mPwdType==PWD_TYPE_NUM){

            }
            //如果是自由说
            else if(mPwdType==PWD_TYPE_FREE){

            }
            // 设置auth_id，不能设置为空
            mVerifier.setParameter(SpeechConstant.AUTH_ID, authId);
            mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
            // 开始验证
            mVerifier.startListening(mVerifyListener);
            break;
            //组管理
        case  R.id.isv_group:
            //跳转到组管理
            Intent intent=new Intent(getContext(),GroupManagerActivity.class);
            intent.putExtra("auth_id",authId);
            intent.putExtra("mfv_scenes","ivp");
            startActivity(intent);

            break;

            default:
                break;
    }
    }

    /*
    获得密码的监听器
     */
    private  String[] items;
    private SpeechListener mPwdListenter=new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            String result = new String(bytes);
            switch (mPwdType) {
                case PWD_TYPE_TEXT:
                    try {
                        JSONObject object = new JSONObject(result);
                        if (!object.has("txt_pwd")) {
                            initTextView();
                            showTip("没有txt_pwd");
                            return;
                        }
                        JSONArray pwdArray = object.optJSONArray("txt_pwd");
                        items = new String[pwdArray.length()];
                        for (int i = 0; i < pwdArray.length(); i++) {

                            items[i] = pwdArray.getString(i);

                        }
                        mTextPwdSelectDialog = new AlertDialog.Builder(getActivity()).setTitle("请选择密码文本")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    mTextPwd=items[which];
                                    mResultEditText.setText("你的密码是："+mTextPwd);
                                    }
                                }).create();
                        mTextPwdSelectDialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case PWD_TYPE_NUM:
                break;
                default:
                    break;
            }
        }
        @Override
        public void onCompleted(SpeechError speechError){
            if(null!=speechError&&ErrorCode.SUCCESS!=speechError.getErrorCode()){
                showTip("获取失败"+speechError.getErrorCode());
            }
        }
    };

    /*
    模型监听器

     */
    private  SpeechListener mModelOperationListener=new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        String result=new String(bytes);
        try{
            JSONObject object=new JSONObject(result);
            //获取到命令行
            String cmd=object.getString("cmd");
            showTip("正在执行"+cmd);
            int ret=object.getInt("ret");
            if("del".equals(cmd)){
             if(ret==ErrorCode.SUCCESS){
                showTip("删除成功");
                mResultEditText.setText("");
             }else if(ret==ErrorCode.MSP_ERROR_FAIL){
                 showTip("模型不存在");
             }
            }
            //如果为查询
            else if("que".equals(cmd)){
                if(ret==ErrorCode.SUCCESS){
                    showTip("模型存在");

                }else if(ret==ErrorCode.MSP_ERROR_FAIL){
                    showTip("模型不存在");
                }
            }
        }catch (JSONException e){
          e.printStackTrace();
        }
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            //获得的不是正确
        if(null!=speechError&&ErrorCode.SUCCESS!=speechError.getErrorCode()){
            showTip("操作失败"+speechError.getPlainDescription(true));
        }
        }
    };
    /*
    识别监听器
     */
    private VerifierListener mVerifyListener=new VerifierListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("当前正在说话，音量大小：" + i);
            Log.d(TAG, "返回音频数据："+bytes.length);
        }

        @Override
        public void onBeginOfSpeech() {
        showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
        showTip("结束说话");
        }

        @Override
        public void onResult(VerifierResult verifierResult) {
        mShowMsgTextView.setText(verifierResult.source);
        if(verifierResult.ret==0){
            mShowMsgTextView.setText("验证通过，识别用户为"+verifierResult.vid);

        }
        else{
            switch (verifierResult.err){
                case VerifierResult.MSS_ERROR_IVP_GENERAL:
                    mShowMsgTextView.setText("内核异常");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                    mShowMsgTextView.setText("出现截幅");
                    break;
                case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                    mShowMsgTextView.setText("太多噪音");
                    break;
                case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                    mShowMsgTextView.setText("录音太短");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                    mShowMsgTextView.setText("验证不通过，您所读的文本不一致");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                    mShowMsgTextView.setText("音量太低");
                    break;
                case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                    mShowMsgTextView.setText("音频长达不到自由说的要求");
                    break;
                default:
                    mShowMsgTextView.setText("验证不通过,相似度仅为"+verifierResult.score+"%。");
                    break;
            }
        }
        }

        @Override
        public void onError(SpeechError speechError) {
            switch (speechError.getErrorCode()) {
                case ErrorCode.MSP_ERROR_NOT_FOUND:
                    mShowMsgTextView.setText("模型不存在，请先注册");
                    break;

                default:
                    showTip("onError Code："    + speechError.getPlainDescription(true));
                    break;
            }

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    /*
    注册监听器
     */
    private  VerifierListener mRegisterListener=new VerifierListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            showTip("当前正在说话，音量大小：" + i);
            Log.d(TAG, "返回音频数据："+bytes.length);
        }

        @Override
        public void onBeginOfSpeech() {
        showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
        showTip("结束说话");
        }

        @Override
        public void onResult(VerifierResult verifierResult) {
            ((TextView)getView().findViewById(R.id.showMsg)).setText(verifierResult.source);
        if(verifierResult.ret==ErrorCode.SUCCESS){
            switch (verifierResult.err) {
                case VerifierResult.MSS_ERROR_IVP_GENERAL:
                    mShowMsgTextView.setText("内核异常");
                    break;
                case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
                    mShowRegFbkTextView.setText("训练达到最大次数");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
                    mShowRegFbkTextView.setText("出现截幅");
                    break;
                case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
                    mShowRegFbkTextView.setText("太多噪音");
                    break;
                case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
                    mShowRegFbkTextView.setText("录音太短");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
                    mShowRegFbkTextView.setText("训练失败，您所读的文本不一致");
                    break;
                case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
                    mShowRegFbkTextView.setText("音量太低");
                    break;
                case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
                    mShowMsgTextView.setText("音频长达不到自由说的要求");
                default:
                    mShowRegFbkTextView.setText("");
                    break;
            }
            if(verifierResult.suc==verifierResult.rgn){
                mShowMsgTextView.setText("注册成功");
                //为文本密码
                if(PWD_TYPE_TEXT==mPwdType){
                    mResultEditText.setText("你的文本密码声纹ID:\n"+verifierResult.vid);
                }
                //数字密码
                else if(PWD_TYPE_NUM==mPwdType){

                }
                //自由说
                else if(PWD_TYPE_FREE==mPwdType){

                }
            }
            else{
              int nowTimes=verifierResult.suc+1;
              int leftTimes=verifierResult.rgn-nowTimes;
              //文本密码
              if(PWD_TYPE_TEXT==mPwdType){
                 mShowPwdTextView.setText("请读出："+mTextPwd);
              }
              //数字密码
              else if(PWD_TYPE_NUM==mPwdType){

              }
              //自由说
              else if(PWD_TYPE_FREE==mPwdType){

              }
              mShowMsgTextView.setText("训练第"+nowTimes+"遍，剩余"+leftTimes+"遍");
            }
        }else{
            mShowMsgTextView.setText("注册失败，请重新开始");
        }
        }

        @Override
        public void onError(SpeechError speechError) {

            if (speechError.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
                showTip("模型已存在，如需重新注册，请先删除");
            } else {
                showTip("onError Code：" + speechError.getPlainDescription(true));
            }

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
  //提示框
    public void showTip(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
    private boolean checkInstance(){
        if( null == mVerifier ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化" );
            return false;
        }else{
            return true;
        }
    }



}
