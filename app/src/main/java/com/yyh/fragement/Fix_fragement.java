package com.yyh.fragement;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.yyh.R;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Fix_fragement extends Fragment {

    private Button savefile;
    private Button read;
    private EditText content;
    private Spinner spinner;
    private Spinner spinner2;
    private List<File> filelist;
    private  String dirpath;
    private  String path=null;
    private String name;
    private PlayerView playerView;
    private ExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private String wavpath[];
    ArrayList<MediaSource> sources ;
    private MyEventlistener myEventlistener;
    private DynamicConcatenatingMediaSource dynamicMediaSource;


  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View view=inflater.inflate(R.layout.fragement_modify,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      init(view);

      //添加消息响应
        //保存响应
        savefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            savefile();
            }
        });
        //加载响应
        read.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    loadfile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });





    }

    private void initPlayer() {
        sources = new ArrayList<>();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultRenderersFactory(getActivity()), new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        playerView.setControllerHideOnTouch(false);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        playerView.setControllerShowTimeoutMs(100000);
        //创建wav文件
        //http://www.170mv.com/kw/other.web.nf01.sycdn.kuwo.cn/resource/n2/29/58/1319188966.mp3

        for(int i=0;i<wavpath.length;i++){
            Uri uri=Uri.parse(wavpath[i]);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getContext(),"MyApplication"));
           MediaSource  mediaSource=new ExtractorMediaSource.Factory(
                    dataSourceFactory).createMediaSource(uri);
            sources.add(mediaSource);
        }

        if (sources!=null) {
            dynamicMediaSource = new DynamicConcatenatingMediaSource();
            dynamicMediaSource.addMediaSources(sources);
            player.addListener(myEventlistener);
            player.prepare(dynamicMediaSource, false, true);

        }else {
            showtip("mediaSource   null");
        }
    }


    //监听器
    private class MyEventlistener implements Player.EventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED:
                    showtip("播放结束");
                    break;
                case Player.STATE_READY:

            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }


    //释放
    private void releasePlayer() {
        if (player != null) {
            //注销监听
            player.removeListener(myEventlistener);
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;

        }
    }
    //初始化下拉菜单
    public void init(View view){

        //首先对组件进行初始化
        savefile=(Button)view.findViewById(R.id.savefile);
        read=(Button)view.findViewById(R.id.read);
        //级联下拉菜单
        spinner=(Spinner)view.findViewById(R.id.spinner);
        spinner2=(Spinner)view.findViewById(R.id.spinner2);
        content=(EditText)view.findViewById(R.id.contrast_content);
        //对播放器进行初始化
        playerView=(PlayerView)view.findViewById(R.id.player_view);
        //监听器初始化
        myEventlistener=new MyEventlistener();

     //首先读取文件夹下的所有文件
        String pathname=Environment.getExternalStorageDirectory() + "/itv/";
        File file=new File(pathname);
        File[]files=file.listFiles();

        if(files==null){
            filelist=new ArrayList<>();
        }
        else{
            //得到文件中的文件名字
            filelist= Arrays.asList(files);
        }
        //获得文件中的文件夹名称
        List<String>dirname =new ArrayList<>();
        for (int i = 0; i < filelist.size(); i++) {
            int start=filelist.get(i).getPath().lastIndexOf("/");
            dirname.add(filelist.get(i).getPath().substring(start+1));

        }

        //然后就是初始化下拉菜单
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,dirname);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //添加下拉菜单的消息响应
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            //进行二级菜单的初始化
                String dir=(String)adapterView.getItemAtPosition(position);
                name=dir;
                //首先读取文件夹下的所有文件
                dirpath=Environment.getExternalStorageDirectory() + "/itv/"+dir;
                File file=new File(dirpath);
                File[]files=file.listFiles();

                if(files==null){
                    filelist=new ArrayList<>();
                }
                else{
                    //得到文件中的文件名字
                    filelist= Arrays.asList(files);
                }
                //获得文件中的文件夹名称
                List<String>filename =new ArrayList<>();
                for (int i = 0; i < filelist.size(); i++) {
                    int start=filelist.get(i).getPath().lastIndexOf("/");
                    filename.add(filelist.get(i).getPath().substring(start+1));

                }
                //然后就是初始化下拉菜单
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,filename);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showtip("请先进行文件的选择");
            }
        });

    }
//文件加载
    public void loadfile() throws FileNotFoundException {
      int num=0;
      FileInputStream fileInputStream=null;
      //首先对面板初始化
        content.setText("");
      String filename=spinner2.getSelectedItem().toString();
      if(filename==null){
          showtip("不存在访谈文件");
      }
      else {
          path = dirpath + "/" + filename;
          //然后读取文件
          try {
              File file = new File(path);
              fileInputStream = new FileInputStream(file);
              byte[] buffer = new byte[1024];
              int len = fileInputStream.read(buffer);
              while (len > 0) {
                  content.append(new String(buffer, 0, len));
                  len = fileInputStream.read(buffer);
              }

          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              try {
                  fileInputStream.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          //初始化音频
          //如果不为空则重设音频
          if (player != null) {
              player.stop(true);
          }
          //读取到语音文件的地址
          String itapath = Environment.getExternalStorageDirectory() + "/iat/" + name + "/samples";
          //showtip(itapath);
          File file = new File(itapath);
          if (file.exists()) {
              File[] files = file.listFiles();
              wavpath=new String[files.length];
           for (File c:files){
               wavpath[num++]=c.getPath();
              }


              Log.d("音频",wavpath.toString());
              //然后初始化exoplayer
              if (content != null) {
                 initPlayer();
                  showtip("已加载完成");
              } else {
                  showtip("加载失败");
              }
          } else {
              showtip("录音文件不存在");
          }
      }
    }
    //保存
    public void savefile() {

        //然后就是把文件传输入新建的文件中


        if (path == null) {
            showtip("请先选择指定的文件");
        }
        else {
            //添加响应提示框
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("提示");
            alertDialog.setMessage("是否保存修改的文件");
            //保存按钮
            alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //首先对文件进行操作
                    BufferedWriter writer = null;
                    try {
                        File file = new File(path);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(content.getText().toString().getBytes());
                        showtip("保存成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //然后清空内容框
                    content.setText("");
                    showtip("保存成功");

                }
            });
            //取消按钮
            alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 showtip("取消成功");
                }
            });

            alertDialog.show();

        }
    }
    public void showtip(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_LONG).show();
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable,100);
        }
    };
    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        if (player!=null) {

            releasePlayer();
        }
        super.onDestroy();
    }
    @Override
    public void onPause() {
        // 暂停播放
        if (player != null) {
            player.stop();
        }
        super.onPause();
    }



}
