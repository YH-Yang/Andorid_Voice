package com.yyh.fragement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.yyh.activity.Main2Activity;
import com.yyh.utils.AutoFitTextureView;
import com.yyh.utils.JsonParser;
import com.yyh.R;
import com.yyh.utils.Statics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class collecting_fragement extends Fragment {

    private Button ask;
    private Button answer;
    private Button clear;
    private  Button end;
    private  Button speakText;
    private TextView it_ask,itv_show,it_im;
    private static final String TAG = collecting_fragement.class .getSimpleName();

    private  Boolean isask=true;
    private  String name;
    private  String data;
    private String filename;
    private  SpeechRecognizer mIat;
    private  int retrunnumber=0;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();
    private boolean mTranslateEnable = false;
    /*  这里开始是有关于视频的元素  VideoRecord Start  */

//    private Chronometer recordTime;
//
//    private static final String[] VIDEO_PERMISSIONS = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//    };
//    //默认的翻转角度
//    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
//    //倒过来的翻转角度
//    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
//    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
//    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
//    private static final int REQUEST_VIDEO_PERMISSIONS = 1;  //指定请求权限值
//    private static final String FRAGMENT_DIALOG = "dialog";  //指定Dialog的名字
//    private Integer mSensorOrientation;
//    private String VideoPath_front,VideoPath_back;
//    private Button record;
//    /**
//     * An {@link AutoFitTextureView} for camera preview.
//     */
//    private AutoFitTextureView texture_front;
//    private AutoFitTextureView texture_back;
//
//    /**
//     * A reference to the opened {@link android.hardware.camera2.CameraDevice}.
//     * mCameraDevice——前摄像头，mCameraDeviceBack——后摄像头
//     */
//    private CameraDevice mCameraDevice,mCameraDeviceBack;
//
//    /**
//     * A reference to the current {@link android.hardware.camera2.CameraCaptureSession} for
//     * preview.
//     */
//    private CameraCaptureSession mPreviewSession_front,mPreviewSession_back;;
//
//    /**
//     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
//     * {@link TextureView}.
//     */
//    private TextureView.SurfaceTextureListener mFrontTextureListener
//            = new TextureView.SurfaceTextureListener() {
//
//        @Override
//        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
//                                              int width, int height) {
//            openCamera(false,width, height);
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
//                                                int width, int height) {
//            configureTransform(false,width, height);
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//            return true;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        }
//
//    };
//    private TextureView.SurfaceTextureListener mBackTextureListener
//            = new TextureView.SurfaceTextureListener() {
//
//        @Override
//        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
//                                              int width, int height) {
//            openCamera(true,width, height);
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
//                                                int width, int height) {
//            configureTransform(true,width, height);
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//            return true;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//        }
//
//    };
//
//
//    /**
//     * The {@link android.util.Size} of camera preview.
//     */
//    private Size mPreviewSize;
//
//    /**
//     * The {@link android.util.Size} of video recording.
//     */
//    private Size mVideoSize;
//
//    /**
//     * MediaRecorder
//     */
//    private MediaRecorder mMediaRecorder_front,mMediaRecorder_back;
//
//    /**
//     * Whether the app is recording video now
//     */
//    private boolean mIsRecordingVideo=false;
//
//    /**
//     * An additional thread for running tasks that shouldn't block the UI.
//     */
//    private HandlerThread mBackgroundThread_front;
//    private HandlerThread mBackgroundThread_back;
//
//    /**
//     * A {@link Handler} for running tasks in the background.
//     */
//    private Handler mHandler_front;
//    private Handler mHandler_back;
//
//    /**
//     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
//     * 一个用于保证在相机关闭前应用存活的信号标（并发）
//     */
//    private Semaphore mCameraOpenCloseLock_back = new Semaphore(1);
//    private Semaphore mCameraOpenCloseLock_front = new Semaphore(1);
//
//    /**
//     * A builder for capture requests.
//     * To obtain a builder instance, use the CameraDevice#createCaptureRequest method, which initializes the request fields to one of the templates defined in CameraDevice.
//     * 捕获请求的构建器。
//     * 要获取构建器实例，请使用该 CameraDevice#createCaptureRequest方法，该方法将请求字段初始化为其中定义的模板之一CameraDevice。
//     */
//    private CaptureRequest.Builder mPreviewBuilder_front,mPreviewBuilder_back;
//
//    /**
//     * CameraDevice是连接在安卓设备上的单个相机的抽象表示
//     * 这里是后摄像头
//     */
//    private CameraDevice.StateCallback mStateCallback_back = new CameraDevice.StateCallback() {
//        @Override
//        public void onOpened(@NonNull CameraDevice cameraDevice) {
//            //当相机打开成功之后会回调此方法
//            //一般在此进行获取一个全局的CameraDevice实例，开启相机预览等操作
//            //获取CameraDevice实例
//            mCameraDeviceBack = cameraDevice;
//            //开启预览
//            startPreview(true);
//            mCameraOpenCloseLock_back.release();
//            if (null != texture_back) {
//                configureTransform(true,texture_back.getWidth(), texture_back.getHeight());
//            }
//        }
//
//        @Override
//        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
//            mCameraOpenCloseLock_back.release();
//            cameraDevice.close();
//            mCameraDeviceBack = null;
//        }
//
//        @Override
//        public void onError(@NonNull CameraDevice cameraDevice, int error) {
//            mCameraOpenCloseLock_back.release();
//            cameraDevice.close();
//            mCameraDeviceBack = null;
//            Activity activity = getActivity();
//            if (null != activity) {
//                activity.finish();
//            }
//        }
//
//    };
//    /**
//     * CameraDevice是连接在安卓设备上的单个相机的抽象表示
//     * 这里是前摄像头
//     */
//    private CameraDevice.StateCallback mStateCallback_front = new CameraDevice.StateCallback() {
//
//        @Override
//        public void onOpened(@NonNull CameraDevice cameraDevice) {
//            mCameraDevice = cameraDevice;
//            startPreview(false);
//            mCameraOpenCloseLock_front.release();
//            if (null != texture_front) {
//                configureTransform(false,texture_front.getWidth(), texture_front.getHeight());
//            }
//        }
//
//        @Override
//        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
//            mCameraOpenCloseLock_front.release();
//            cameraDevice.close();
//            mCameraDevice = null;
//        }
//
//        @Override
//        public void onError(@NonNull CameraDevice cameraDevice, int error) {
//            mCameraOpenCloseLock_front.release();
//            cameraDevice.close();
//            mCameraDevice = null;
//            Activity activity = getActivity();
//            if (null != activity) {
//                activity.finish();
//            }
//        }
//
//    };
//
//    private Handler recordHandler=new Handler(){
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what){
//                case 1:
//                    if (mIsRecordingVideo) {
//                        stopRecordingVideo();
//                    } else {
//                        startRecordingVideo();
//                    }
//                default:
//            }
//        }
//    };
    /*  这里是有关于视频的元素  VideoRecord End  */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


     View view=inflater.inflate(R.layout.framgement_collecting,container,false);
     return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

         // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
       // SpeechUtility.createUtility(getContext(), SpeechConstant.APPID +"=5cff96ac");
        SpeechUtility.createUtility(getContext(), SpeechConstant.APPID +"=5d4d7462");
        mIatDialog = new RecognizerDialog(getActivity(), mInitListener);
        //初始化文本框
        it_ask=(TextView)view.findViewById(R.id.tv_ask);
        itv_show=(TextView)view.findViewById(R.id.itv_show);
        itv_show.setMovementMethod(ScrollingMovementMethod.getInstance());
        it_im=(TextView)view.findViewById(R.id.tv_im);
//        record = (Button) view.findViewById(R.id.record);
//        recordTime = (Chronometer)view.findViewById(R.id.recordTime);
//        texture_front = view.findViewById(R.id.texture_front);
//        texture_back=view.findViewById(R.id.texture_back);
        //初始化当前的笔录信息 从sharepreferences中获得数据
        try {
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
            name = sharedPreferences.getString("name","默认名称");
            data =sharedPreferences.getString("date","默认日期");

        }catch (Exception e){
          e.printStackTrace();
        }

        it_im.setText("时间:"+data+" 笔录名称:"+name);
        //初始化按钮
        ask=(Button)view.findViewById(R.id.ask);
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               isask=true;
              speak();


            }
        });
        answer=(Button)view.findViewById(R.id.answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             isask=false;
            speak();
            }
        });
        clear=(Button)view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             itv_show.setText("");
            }
        });
        end=(Button)view.findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            speakend();
            }
        });
        speakText=(Button)view.findViewById(R.id.speakText);
        speakText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               speakText();

            }
        });

//        record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recordHandler.sendEmptyMessage(1);
//            }
//        });

        if (name!=null) {
            //创建访谈文件夹
            File file = new File(Environment.getExternalStorageDirectory() + "/msc/iat/" + name);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
    private void speakText(){
        //创建对象
        SpeechSynthesizer mTts=SpeechSynthesizer.createSynthesizer(getActivity(),null);
        //整合参数
        mTts.setParameter(SpeechConstant. VOICE_NAME, "vixyun" ); // 设置发音人
        mTts.setParameter(SpeechConstant. SPEED, "50" );// 设置语速
        mTts.setParameter(SpeechConstant. VOLUME, "80" );// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant. ENGINE_TYPE, SpeechConstant. TYPE_CLOUD);

        //设置保存的位置
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,"./sdcard/iflytek.pcm");
        mTts.startSpeaking( itv_show.getText().toString(), new MySynthesizerListener()) ;
    }
    class MySynthesizerListener implements SynthesizerListener{

        @Override
        public void onSpeakBegin() {
            ShowTip("开始播放");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {
        ShowTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
        ShowTip("继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if(speechError==null){
              ShowTip("播放完成");
            }
            else if(speechError!=null){
                //显示错误的结果
                ShowTip(speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ShowTip("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    //结束谈话
    private void speakend(){

        //首先暂停监听
        //首先判断是否打开了监听
        if(mIat!=null) {
            mIat.stopListening();
        }
        //首先弹出消息框让用户判断是否进行结束操作
        AlertDialog.Builder makesure=new AlertDialog.Builder(getActivity());
        makesure.setTitle("提示");
        makesure.setMessage("是否结束本次谈话");
        //首先设立确定按钮
        makesure.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                BufferedWriter writer=null;
                //然后就是把文件传输入新建的文件中
                try {
                    //首先创建文件夹
                    File f=new File(Environment.getExternalStorageDirectory() +"/itv/"+name);
                    f.mkdirs();
                    //文件操作
                    filename=data+name+".txt";
                    String filepath=Environment.getExternalStorageDirectory() +"/itv/"+name+"/"+filename;
                    File file=new File(filepath);
                    //创建文件
                    file.createNewFile();
                    FileOutputStream outputStream=new FileOutputStream(file);
                    outputStream.write(itv_show.getText().toString().getBytes());

                    ShowTip("保存成功" );
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(writer!=null){
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //把sharepreference 中的值变为空
                SharedPreferences.Editor editor=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                editor.putString("name","");
                editor.putString("date","");
                editor.apply();

                //切换到设置页面
                Setting_fragement setting_fragement=new Setting_fragement();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.right_layout,setting_fragement);
                fragmentTransaction.commit();



            }
        });
        //设置再次按钮
        makesure.setNegativeButton("再来一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                retrunnumber++;
                name+="("+retrunnumber+")";
                //清空消息框
                itv_show.setText("");
                ShowTip("请继续操作");
            }
        });
        //设置提示框不可返回消失
        makesure.setCancelable(false).create();
        //显示提示框
        makesure.show();

    }




    private void speak(){
        //开始显示听写界面
       // mIatDialog.show();
        //创建识别对象
        SpeechRecognizer mIat=SpeechRecognizer.createRecognizer(getActivity(),null);
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言设置为普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置为短信与日常用语
        mIat.setParameter(SpeechConstant.DOMAIN,"iat");
        mIat.startListening(mRecoListener);
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        //设置文件录音的存储地点
        //名字为当前的时间
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iat/"+name+"/samples/"+ System.currentTimeMillis()+".wav");



    }


    private RecognizerListener mRecoListener=new RecognizerListener() {
        // 听写结果回调接口 (返回Json 格式结果，用户可参见附录 13.1)；
    //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
    //关于解析Json的代码可参见 Demo中JsonParser 类；
    //isLast等于true 时会话结束。

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
        ShowTip("开始录音");
        }

        @Override
        public void onEndOfSpeech() {
            it_ask.setText("请按开始并说话");
        ShowTip("结束录音");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {

                String result=recognizerResult.getResultString();
                //ShowTip(result);
                System.out.print("没解析的："+result);
                String text=JsonParser.parseIatResult(result);
                System.out.print("解析后："+text);
                String sn=null;
                try{
                    JSONObject resultJson=new JSONObject(recognizerResult.getResultString());
                    sn=resultJson.optString("sn");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                mIatResults.put(sn,text);//每得到一句话添加到结果文本中
                StringBuffer resultBuffer=new StringBuffer();
                if(isask) {
                    resultBuffer.append("问：");
                }
                else{
                    resultBuffer.append("答：");
                }
                for(String key:mIatResults.keySet()){
                    resultBuffer.append(mIatResults.get(key));
                }
                it_ask.setTextColor(Color.RED);
                it_ask.setText(resultBuffer.toString());
                if(b) {
                    itv_show.append(it_ask.getText().toString() + "\n");//设置输入框的文本
                }
        }

        @Override
        public void onError(SpeechError speechError) {
            ShowTip(speechError.getPlainDescription(true)) ;
            // 获取错误码描述
            Log. e(TAG, "error.getPlainDescription(true)==" + speechError.getPlainDescription(true ));

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    private void ShowTip(String data) {
        Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
    }
    /*  这里开始是有关于视频的内容  VideoRecord Start  */

    /**
     * Requests permissions needed for recording video.
     * 获取录制视频所需的权限
     */
//    private void requestVideoPermissions() {
//        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS.toString())) {
//            new ConfirmationDialog().show(getChildFragmentManager(),FRAGMENT_DIALOG);
//        } else {
//            requestPermissions( VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
//        }
//    }
//
//    /**
//     * 开启相机预览的方法
//     * @param isBack  1为后摄像头，0为前摄像头
//     * @param width  预览框宽度
//     * @param height  预览框长度
//     */
//    @SuppressLint("MissingPermission")
//    private void openCamera(boolean isBack,int width, int height) {
//        //检查权限是否获取成功
//        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
//            requestVideoPermissions();
//            return;
//        }
//        final Activity activity = getActivity();
//        //获取当前活动Activity，获取不到即return停止运行
//        if (null == activity || activity.isFinishing()) {
//            return;
//        }
//        //获取CameraManager  相机管理者
//        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            //获取信号标告诉系统开启相机
//            if (!getSemaphore(isBack).tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//            //要打开的摄像头ID
//            String cameraId;
//            //isBack = 1后   0前
//            //是后置摄像头的情况
//            if (isBack){
//                //获取到相机的ID 0后 1前
//                cameraId = manager.getCameraIdList()[0];
//                // Choose the sizes for camera preview and video recording
//                //选择相机预览以及视频录制的sizes
//                //此处获取到的CameraCharacteristics是描述相机设备的属性类
//                //获取指定摄像头的特性（属性）
//                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//                //获取摄像头支持的配置属性
//                StreamConfigurationMap map = characteristics
//                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                //顺时针角度
//                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//                //如果获取不到配置属性，则throw一个无法正常预览的错误
//                if (map == null) {
//                    throw new RuntimeException("Cannot get available preview/video sizes");
//                }
//                //选择录制视频的尺寸
//                mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
//                //获取最佳的长宽比预览尺寸
//                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
//                        width, height, mVideoSize);
//                //获取屏幕的旋转方向
//                int orientation = getResources().getConfiguration().orientation;
//                //如果是横屏的情况 LANDSCAPE=横屏
//                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    //设置预览组件的宽高比，横屏的情况下屏宽为宽，屏高为高
//                    texture_back.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//                } else {
//                    //如果是竖屏的情况下，竖屏的情况下屏高为宽，屏宽为高
//                    texture_back.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
//                }
//
//                configureTransform(true,width, height);
//                mMediaRecorder_back = new MediaRecorder();
//                manager.openCamera(cameraId, mStateCallback_back, null);
//            }else {
//                //是前置摄像头的情况
//                cameraId = manager.getCameraIdList()[1];     //0后   1前
//                // Choose the sizes for camera preview and video recording
//                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//                StreamConfigurationMap map = characteristics
//                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//                if (map == null) {
//                    throw new RuntimeException("Cannot get available preview/video sizes");
//                }
//                mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
//                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
//                        width, height, mVideoSize);
//                int orientation = getResources().getConfiguration().orientation;
//                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    texture_front.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//                } else {
//                    texture_front.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
//                }
//                configureTransform(false,width, height);
//                mMediaRecorder_front = new MediaRecorder();
//                manager.openCamera(cameraId, mStateCallback_front, null);
//            }
//
//        } catch (CameraAccessException e) {
//            ShowTip("Cannot access the camera.");
//            activity.finish();
//        } catch (NullPointerException e) {
//            // Currently an NPE is thrown when the Camera2API is used but not supported on the
//            // device this code runs.
//            ErrorDialog.newInstance(getString(R.string.camera_error))
//                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Interrupted while trying to lock camera opening.");
//        }
//    }
//
//    /**
//     * 关闭摄像头的方法
//     */
//    private void closeCamera() {
//        try {
//            mCameraOpenCloseLock_back.acquire();
//            mCameraOpenCloseLock_front.acquire();
//            closePreviewSession(true);
//            closePreviewSession(false);
//            if (null != mCameraDevice) {
//                mCameraDevice.close();
//                mCameraDevice = null;
//            }
//            if (null != mCameraDeviceBack) {
//                mCameraDeviceBack.close();
//                mCameraDeviceBack = null;
//            }
//            if (null != mMediaRecorder_front) {
//                mMediaRecorder_front.release();
//                mMediaRecorder_front = null;
//            }
//            if (null != mMediaRecorder_back) {
//                mMediaRecorder_back.release();
//                mMediaRecorder_back = null;
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Interrupted while trying to lock camera closing.");
//        } finally {
//            mCameraOpenCloseLock_back.release();
//            mCameraOpenCloseLock_front.release();
//        }
//    }
//
//    /**
//     * 检测权限是否通过
//     * @param permissions 权限值数组
//     * @return 真为通过，假为拒绝
//     */
//    private boolean hasPermissionsGranted(String[] permissions) {
//        for (String permission : permissions) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Compares two {@code Size}s based on their areas.
//     * 比较两个Size
//     */
//    static class CompareSizesByArea implements Comparator<Size> {
//
//        @Override
//        public int compare(Size lhs, Size rhs) {
//            // We cast here to ensure the multiplications won't overflow
//            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
//                    (long) rhs.getWidth() * rhs.getHeight());
//        }
//
//    }
//
//    /**
//     * 翻译：配置转换
//     * @param isBack
//     * @param viewWidth
//     * @param viewHeight
//     */
//    private void configureTransform(boolean isBack,int viewWidth, int viewHeight) {
//        Activity activity = getActivity();
//        if (null ==getTexture(isBack) || null == mPreviewSize || null == activity) {
//            return;
//        }
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
//            float scale = Math.max(
//                    (float) viewHeight / mPreviewSize.getHeight(),
//                    (float) viewWidth / mPreviewSize.getWidth());
//            matrix.postScale(scale, scale, centerX, centerY);
//            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
//        }
//        getTexture(isBack).setTransform(matrix);
//    }
//
//    /**
//     * 返回AutoFitTextureView
//     * @param isBack 用于判断前置还是后置的参数，真为后假为前
//     * @return 返回AutoFitTextureView
//     */
//    private AutoFitTextureView getTexture(boolean isBack){
//        if (isBack){
//            return texture_back;
//        }else {
//            return texture_front;
//        }
//    }
//
//    /**
//     * 获取并发信号标  TODO：这里还有不懂的地方
//     * @param isBack  0为前摄像头，1为后摄像头
//     * @return  返回值为指定的信号标
//     */
//    private Semaphore getSemaphore(Boolean isBack){
//        if (isBack){
//            return mCameraOpenCloseLock_back;
//        }else {
//            return mCameraOpenCloseLock_front;
//        }
//    }
//
//    /**
//     * 获取摄像头Session
//     * CameraCaptureSession 是一个事务，用来向相机设备发送获取图像的请求。
//     * @param isBack
//     * @return 返回指定的CameraCaptureSession
//     */
//    private CameraCaptureSession getCameraSession(boolean isBack) {
//        if (isBack) {
//            return mPreviewSession_back;
//        } else {
//            return mPreviewSession_front;
//        }
//    }
//
//    /**
//     *获取CameraDevice
//     * @param isBack 判断前后摄像头
//     * @return 返回指定的CameraDevice
//     */
//    private CameraDevice getDevices(Boolean isBack) {
//        if (isBack){
//            return mCameraDeviceBack;
//        }else
//            return mCameraDevice;
//    }
//
//    /**
//     *
//     * @param isBack
//     * @return
//     */
//    private Handler getHandler(boolean isBack){
//        if (isBack){
//            return mHandler_back;
//        }else {
//            return mHandler_front;
//        }
//    }
//
//    /**
//     * 获取CaptureRequest.Builder
//     * @param isBack 判断是前后摄像头
//     * @return 返回捕获请求的builder
//     */
//    private CaptureRequest.Builder getBuilder(Boolean isBack) {
//        if (isBack){
//            return mPreviewBuilder_back;
//        }else {
//            return mPreviewBuilder_front;
//        }
//    }
//
//    /**
//     * 选择视频录制的Size
//     * @param choices  一个Size数组
//     * @return size
//     */
//    private static Size chooseVideoSize(Size[] choices) {
//        for (Size size : choices) {
//            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
//                return size;
//            }
//        }
//        Log.e( "logE","Couldn't find any suitable video size");
//        return choices[choices.length - 1];
//    }
//
//    /**
//     * 获取最佳的长宽比预览尺寸
//     * @param choices
//     * @param width
//     * @param height
//     * @param aspectRatio
//     * @return
//     */
//    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
//        // Collect the supported resolutions that are at least as big as the preview Surface
//        //选择合适的长宽比的分辨率
//        List<Size> bigEnough = new ArrayList<>();
//        int w = aspectRatio.getWidth();
//        int h = aspectRatio.getHeight();
//        for (Size option : choices) {
//            if (option.getHeight() == option.getWidth() * h / w &&
//                    option.getWidth() >= width && option.getHeight() >= height) {
//                bigEnough.add(option);
//            }
//        }
//        // Pick the smallest of those, assuming we found any
//        if (bigEnough.size() > 0) {
//            return Collections.min(bigEnough, new CompareSizesByArea());
//        } else {
//            Log.e("logE" ,"Couldn't find any suitable preview size");
//            return choices[0];
//        }
//    }
//
//    /**
//     * 创建一个错误提示Dialog
//     */
//    public static class ErrorDialog extends DialogFragment {
//
//        private static final String ARG_MESSAGE = "message";
//
//        public static ErrorDialog newInstance(String message) {
//            ErrorDialog dialog = new ErrorDialog();
//            Bundle args = new Bundle();
//            args.putString(ARG_MESSAGE, message);
//            dialog.setArguments(args);
//            return dialog;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Activity activity = getActivity();
//            return new AlertDialog.Builder(activity)
//                    .setMessage(getArguments().getString(ARG_MESSAGE))
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            activity.finish();
//                        }
//                    })
//                    .create();
//        }
//    }
//
//    /**
//     * 创建一个确认提示Dialog
//     */
//    public static class ConfirmationDialog extends DialogFragment {
//        @NonNull
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Fragment parent = getParentFragment();
//            return new AlertDialog.Builder(getActivity())
//                    .setMessage(R.string.permission_request)
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            parent.requestPermissions( VIDEO_PERMISSIONS,
//                                    REQUEST_VIDEO_PERMISSIONS);
//                        }
//                    })
//                    .setNegativeButton(android.R.string.cancel,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    parent.getActivity().finish();
//                                }
//                            })
//                    .create();
//        }
//
//    }
//
//    /**
//     * 开启预览
//     * @param isBack 判断为是否为前后置摄像头
//     */
//    private void startPreview(final Boolean isBack) {
//        //先判断Texture是否可用与摄像头是否已经正确开启
//        if ( !getTexture(isBack).isAvailable() || null == mPreviewSize) {
//            return;
//        }
//        //线程的名字
//        final String threadName;
//        //通过判断前后摄像头，分别建立一个新的线程
//        if (isBack){
//            threadName="Camera_back_preview";
//        }else {
//            threadName="Camera_front_preview";
//        }
//        try {
//            //关闭预览事务
//            closePreviewSession(isBack);
//            //获取指定的texture用于预览
//            SurfaceTexture texture = getTexture(isBack).getSurfaceTexture();
//            assert texture != null;
//            //设置默认的流Size
//            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//            //通过CameraDevice建立CaptureRequest（捕获请求）
//            if (isBack) {
//                mPreviewBuilder_back = getDevices(isBack).createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            }else {
//                mPreviewBuilder_front = getDevices(isBack).createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            }
//            //通过texture建立预览Surface
//            Surface previewSurface = new Surface(texture);
//            //获取到CaptureRequest的builder，并将surface添加到此请求的目标列表中，用于预览
//            getBuilder(isBack).addTarget(previewSurface);
//            //获取到CameraDevice，创建捕获事务，同时设计一个回调来接收捕获到的东西
//            getDevices(isBack).createCaptureSession(Collections.singletonList(previewSurface),
//                    new CameraCaptureSession.StateCallback() {
//
//                        /**
//                         * This method is called when the camera device has finished configuring itself, and the session can start processing capture requests.
//                         * 当摄像机设备完成自身配置时，将调用此方法，并且会话可以开始处理捕获请求。
//                         * @param session 捕获事务
//                         */
//                        @Override
//                        public void onConfigured(@NonNull CameraCaptureSession session) {
//                            //将当前的事务通过isBack参数判断后赋给相对应的值
//                            if (isBack) {
//                                mPreviewSession_back = session;
//                            }else {
//                                mPreviewSession_front=session;
//                            }
//                            //updatePreview
//                            if (null == getDevices(isBack)) {
//                                return;
//                            }
//                            try {
//                                //对捕获事务进行设置（调为全自动模式）
//                                setUpCaptureRequestBuilder(getBuilder(isBack));
//                                //建立一个新的线程
//                                HandlerThread thread = new HandlerThread(threadName);
//                                //开启线程
//                                thread.start();
//                                //setRepeatingRequest()——请求通过此捕获会话无休止地重复捕获图像，即一直捕获
//                                if (isBack) {
//                                    mPreviewSession_back.setRepeatingRequest(mPreviewBuilder_back.build(), null, mHandler_back);
//                                }else {
//                                    mPreviewSession_front.setRepeatingRequest(mPreviewBuilder_front.build(), null, mHandler_front);
//                                }
//                            } catch (CameraAccessException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//                            Activity activity = getActivity();
//                            if (null != activity) {
//                                //在配置失败时，报出一个错误并Toast显示出来
//                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }, getHandler(isBack));
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 关闭预览Session事务
//     * CameraCaptureSession 是一个事务，用来向相机设备发送获取图像的请求。
//     * @param isBack 判断是前还是后摄像头
//     */
//    private void closePreviewSession(boolean isBack) {
//        if (getCameraSession(isBack)!= null) {
//            getCameraSession(isBack).close();
//            if (isBack){
//                mPreviewSession_back=null;
//            }else {
//                mPreviewSession_front=null;
//            }
//        }
//    }
//
//    /**
//     * 对CaptureRequest进行设置，调成全自动模式
//     * @param builder CaptureRequest的Builder
//     */
//    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
//        //Overall mode of 3A (auto-exposure, auto-white-balance, auto-focus) control routines.
//        //将摄像头设置为全自动模式
//        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
//    }
//
//    /*分割线 上面是有关于预览的部分*/
//
//    /*分割线 下面是有关于录像的部分*/
//
//    /**
//     * 停止录制的方法
//     */
//    private void stopRecordingVideo() {
//        // UI
//        mIsRecordingVideo = false;
//        //按钮更改文字
//        record.setText("开始录制");
//        // Stop recording
//        //停止录制
//        mMediaRecorder_front.stop();
//        mMediaRecorder_front.reset();
//        mMediaRecorder_back.stop();
//        mMediaRecorder_back.reset();
//        //清除录制计数器
//        recordTime.stop();
//        recordTime.setBase(SystemClock.elapsedRealtime());
//        recordTime.setVisibility(View.GONE);
//
//        Activity activity = getActivity();
//        if (null != activity) {
//            //提示视频已经存储在XX路径
//            Toast.makeText(activity, "Video saved: " + Statics.PATH_VIDEO,
//                    Toast.LENGTH_SHORT).show();
//            Log.d("logD" ,"Video saved: " + Statics.PATH_VIDEO);
//        }
//        //将前置与后置摄像头的录像存储路径清空
//        VideoPath_front = null;
//        VideoPath_back=null;
//        //停止录制后，开始预览
//        startPreview(false);
//        startPreview(true);
//    }
//
//    /**
//     * 开始录制视频，前后一起同时录
//     */
//    private void startRecordingVideo() {
//        //先检测需要的对象是否已经准备就绪
//        if (null == mCameraDevice||null == mCameraDeviceBack ||!texture_front.isAvailable() || null == mPreviewSize) {
//            return;
//        }
//        try {
//            //关闭预览事务
//            closePreviewSession(false);
//            closePreviewSession(true);
//            //设置媒体录制器 第一个是前置，第二个是后置
//            setUpMediaRecorder(false);
//            setUpMediaRecorder(true);
//            //获取到前置的SurfaceTexture
//            SurfaceTexture texture_front = this.texture_front.getSurfaceTexture();
//            assert texture_front != null;
//            //设置好默认流参数
//            texture_front.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//            //获取到后置的SurfaceTexture
//            SurfaceTexture texture_back = this.texture_back.getSurfaceTexture();
//            assert texture_back != null;
//            //设置好默认流参数
//            texture_back.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//
//            //为前后摄像头分别以录制参数创建CaptureRequest
//            mPreviewBuilder_front = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//            mPreviewBuilder_back = mCameraDeviceBack.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
//            List<Surface> surfaces_front = new ArrayList<>();
//            List<Surface> surfaces_back = new ArrayList<>();
//
//            // Set up Surface for the camera preview
//            //设置用于预览的Surface
//            Surface previewSurface_front = new Surface(texture_front);
//            Surface previewSurface_back= new Surface(texture_back);
//            surfaces_front.add(previewSurface_front);
//            surfaces_back.add(previewSurface_back);
//            //将这两个Surface添加为CaptureRequest的目标源
//            mPreviewBuilder_front.addTarget(previewSurface_front);
//            mPreviewBuilder_back.addTarget(previewSurface_back);
//
//            // Set up Surface for the MediaRecorder
//            //为视频录制器设置好两个surface用于展示
//            Surface recorderSurface_front = mMediaRecorder_front.getSurface();
//            Surface recorderSurface_back = mMediaRecorder_back.getSurface();
//            surfaces_front.add(recorderSurface_front);
//            surfaces_back.add(recorderSurface_back);
//            mPreviewBuilder_front.addTarget(recorderSurface_front);
//            mPreviewBuilder_back.addTarget(recorderSurface_back);
//
//            // Start a capture session
//            //开启一个捕获事务
//            // Once the session starts, we can update the UI and start recording
//            //一旦事务开启，即可以更新UI并且开始录制
//            mCameraDevice.createCaptureSession(surfaces_front, new CameraCaptureSession.StateCallback() {
//
//                /**
//                 * 成功配置时运行的方法
//                 * @param cameraCaptureSession
//                 */
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    mPreviewSession_front = cameraCaptureSession;
//                    //updatePreview();
//                    //检查CameraDevice是否配置完成
//                    if (null == mCameraDevice) {
//                        return;
//                    }
//                    try {
//                        //对捕获请求进行配置 配置为全自动模式
//                        setUpCaptureRequestBuilder(mPreviewBuilder_front);
//                        //建立一个新的处理线程
//                        HandlerThread thread = new HandlerThread("Camera_front_preview");
//                        thread.start();
//                        //setRepeatingRequest()——请求通过此捕获会话无休止地重复捕获图像，即一直捕获
//                        mPreviewSession_front.setRepeatingRequest(mPreviewBuilder_front.build(), null, mHandler_front);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                /**
//                 * 配置失败的情况下
//                 * @param cameraCaptureSession
//                 */
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    Activity activity = getActivity();
//                    if (null != activity) {
//                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            },mHandler_front);
//
//            mCameraDeviceBack.createCaptureSession(surfaces_back, new CameraCaptureSession.StateCallback() {
//
//                /**
//                 * 成功配置时运行的方法
//                 * @param cameraCaptureSession
//                 */
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    mPreviewSession_back = cameraCaptureSession;
//                    //updatePreview();
//                    if (null == mCameraDeviceBack) {
//                        return;
//                    }
//                    try {
//                        //对捕获请求进行配置 配置为全自动模式
//                        setUpCaptureRequestBuilder(mPreviewBuilder_back);
//                        //建立一个新的处理线程
//                        HandlerThread thread = new HandlerThread("Camera_back_preview");
//                        thread.start();
//                        //setRepeatingRequest()——请求通过此捕获会话无休止地重复捕获图像，即一直捕获
//                        mPreviewSession_back.setRepeatingRequest(mPreviewBuilder_back.build(), null, mHandler_back);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                /**
//                 * 配置失败的情况下
//                 * @param cameraCaptureSession
//                 */
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
//                    Activity activity = getActivity();
//                    if (null != activity) {
//                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            },mHandler_back);
//        } catch (CameraAccessException | IOException e) {
//            e.printStackTrace();
//        }
//        //上面的是在录像期间开启预览surface的方法
//        //开始录像有关部分
//        //开启一条新的线程
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // UI
//                mIsRecordingVideo = true;
//                record.setText("停止录制");
//                // Start recording
//                mMediaRecorder_back.start();
//                // Start recording
//                mMediaRecorder_front.start();
//                //将录制时间显示出来
//                recordTime.setVisibility(View.VISIBLE);
//                recordTime.setBase(SystemClock.elapsedRealtime());
//                recordTime.setFormat("正在录制：%s");
//                recordTime.start();
//            }
//        });
//    }
//
//    /**
//     * 获取媒体录制器
//     * @param isBack 判断前后摄像头
//     * @return 返回相对应的MediaRecorder
//     */
//    private MediaRecorder getMediaRecorder(boolean isBack){
//        return isBack?mMediaRecorder_back:mMediaRecorder_front;
//    }
//
//    /**
//     * 获取文件录制路径
//     * @param isBack 判断前后摄像头
//     * @return 返回处理过后的路径
//     * TODO:如果Statics中的路径发生变化，这里也需要改变
//     */
//    private String getVideoFilePath(boolean isBack) {
//        final File dir = Environment.getExternalStorageDirectory();
//
//        if (isBack) {
//            return (dir == null ? "" : (dir.getAbsolutePath() + "/msc/iat/"))
//                    + name + "/" + data + name + "_back.mp4";
//        }else {
//            return  (dir == null ? "" : (dir.getAbsolutePath() + "/msc/iat/"))
//                    + name + "/" + data + name + "_front.mp4";
//        }
//    }
//
//    /**
//     * 设置媒体录制器
//     * @param isBack 判断前后摄像头
//     * @throws IOException
//     */
//    private void setUpMediaRecorder(boolean isBack) throws IOException {
//        final Activity activity = getActivity();
//        if (null == activity) {
//            return;
//        }
//        //通过isBack参数，获取相对应的媒体录制器
//        if(isBack){
//            //音频来源设置为麦克风
//            //后置摄像头录制记录声音，前置不记录（考虑到双路麦克风比较少）
//            getMediaRecorder(isBack).setAudioSource(MediaRecorder.AudioSource.MIC);
//        }
//        //视频来源设置为Surface
//        getMediaRecorder(isBack).setVideoSource(MediaRecorder.VideoSource.SURFACE);
//        //录制格式设置为MP4
//        getMediaRecorder(isBack).setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//
//
//        //通过isBack判断相对应的摄像头，然后获取到视频存储路径
//        if (isBack){
//            if (VideoPath_back == null || VideoPath_back.isEmpty()) {
//                VideoPath_back = getVideoFilePath(isBack);
//            }
//            //设置输出路径
//            mMediaRecorder_back.setOutputFile(VideoPath_back);
//        }else {
//            if (VideoPath_front == null || VideoPath_front.isEmpty()) {
//                VideoPath_front = getVideoFilePath(isBack);
//            }
//            //设置输出路径
//            mMediaRecorder_front.setOutputFile(VideoPath_front);
//        }
//        //比特率设置
//        getMediaRecorder(isBack).setVideoEncodingBitRate(512*1024);
//        //视频录制帧率设置
//        getMediaRecorder(isBack).setVideoFrameRate(30);
//        //视频长宽高设置
//        getMediaRecorder(isBack).setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
//        //视频编码设置 H264
//        getMediaRecorder(isBack).setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        if(isBack){
//            //音频编码设置
//            getMediaRecorder(isBack).setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        }
//        //获取现在屏幕翻转模式
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        switch (mSensorOrientation) {
//            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
//                //Sets the orientation hint for output video playback.
//                //设置输出视频播放的方向提示。这里设置好将告诉视频播放器要以什么方向来播放才是正确的
//                getMediaRecorder(isBack).setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
//                break;
//            case SENSOR_ORIENTATION_INVERSE_DEGREES:
//                getMediaRecorder(isBack).setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
//                break;
//        }
//        //设置为准备好了的状态
//        getMediaRecorder(isBack).prepare();
//    }
//
//    /**
//     * 开启线程
//     */
//    private void startBackgroundThread() {
//        mBackgroundThread_front = new HandlerThread("Camera_front");
//        mBackgroundThread_front.start();
//        mHandler_front = new Handler(mBackgroundThread_front.getLooper());
//
//        mBackgroundThread_back=new HandlerThread("Camera_back");
//        mBackgroundThread_back.start();
//        mHandler_back=new Handler(mBackgroundThread_back.getLooper());
//    }
//
//    /**
//     * 停止背景线程
//     */
//    private void stopBackgroundThread() {
//        if (mBackgroundThread_front!=null) {
//            mBackgroundThread_front.quitSafely();
//            try {
//                mBackgroundThread_front.join();
//                mBackgroundThread_front = null;
//                mHandler_front = null;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (mBackgroundThread_back!=null) {
//            mBackgroundThread_back.quitSafely();
//            try {
//                mBackgroundThread_back.join();
//                mBackgroundThread_back = null;
//                mHandler_back = null;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /*  这里是有关于视频的内容  VideoRecord End  */
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (texture_front.isAvailable()) {
//            openCamera(false,texture_front.getWidth(), texture_front.getHeight());
//        } else {
//            texture_front.setSurfaceTextureListener(mFrontTextureListener);
//        }
//        if (texture_back.isAvailable()) {
//            openCamera(true,texture_back.getWidth(), texture_back.getHeight());
//        } else {
//            texture_back.setSurfaceTextureListener(mBackTextureListener);
//        }
//        startBackgroundThread();
//    }
//
//    @Override
//    public void onPause() {
//        closeCamera();
//        stopBackgroundThread();
//        super.onPause();
//    }
}
