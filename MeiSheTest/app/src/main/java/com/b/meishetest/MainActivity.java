package com.b.meishetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsStreamingContext;

public class MainActivity extends AppCompatActivity implements NvsStreamingContext.CaptureDeviceCallback,
NvsStreamingContext.CaptureRecordingStartedCallback{

    private NvsStreamingContext mStreamingContext;
    private NvsLiveWindowExt mLiveWindow;
    private int mCurrentDeviceIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化sdk
        NvsStreamingContext.init(this, null, NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT);

        setContentView(R.layout.activity_main);

        mStreamingContext = NvsStreamingContext.getInstance();
        mLiveWindow = (NvsLiveWindowExt) findViewById(R.id.live);

        mStreamingContext.setCaptureDeviceCallback(this);
        // 将采集预览输出连接到LiveWindow控件
        if (!mStreamingContext.connectCapturePreviewWithLiveWindowExt(mLiveWindow)) {
            Log.e("sss", "Failed to connect capture preview with livewindow!");
            return;
        }
        int deviceCount = mStreamingContext.getCaptureDeviceCount();
        Log.e("sss", "deviceCount = " + deviceCount);
        if (deviceCount == 0) return;
        mCurrentDeviceIndex = 0;
        startCapturePreview();
        mStreamingContext.removeAllCaptureVideoFx();

        final Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                    mStreamingContext.stopRecording();
                    button.setText("start");
                } else {
                    String path = PathUtils.getRecordVideoPath();
                    Log.d("sss", "path = " + path);
                    if (!mStreamingContext.startRecording(path)) return;
                    button.setText("stop");
                }
            }
        });

        final Button pause = (Button) findViewById(R.id.btn_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStreamingContext.isRecordingPaused()) {
                    mStreamingContext.resumeRecording();
                    pause.setText("start...");
                } else {
                    if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                        mStreamingContext.pauseRecording();
                        pause.setText("pausing...");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStreamingContext != null) {
            mStreamingContext.close();
        }
    }


    private boolean startCapturePreview() {
        int state = getCurrentEngineState();
        /*if (state != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW) {
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
                    NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH,
                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME
                            | NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE, null)) {
                return false;
            }
            return true;
        }*/
        if (state != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW) {
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
                    NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH,
                    0, null)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    @Override
    public void onCaptureDeviceCapsReady(int i) {
        Log.d("sss", "onCaptureDeviceCapsReady()... " + i);
    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {
        Log.d("sss", "onCaptureDevicePreviewResolutionReady()... " + i);
    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {
        Log.d("sss", "onCaptureDevicePreviewStarted()... " + i);
    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {
        Log.d("sss", "onCaptureDeviceError()... " + i);
    }

    @Override
    public void onCaptureDeviceStopped(int i) {
        Log.d("sss", "onCaptureDeviceStopped()... " + i);
    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {
        Log.d("sss", "onCaptureDeviceAutoFocusComplete()... " + i + "  b = " + b);
    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        Log.d("sss", "onCaptureRecordingFinished()... " + i);
    }

    @Override
    public void onCaptureRecordingError(int i) {
        Log.d("sss", "onCaptureRecordingError()... " + i);
    }

    @Override
    public void onCaptureRecordingStarted(int i) {

    }
}
