package com.jop.jopscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by panwenjuan on 18-5-25.
 */

/**
 * 需要注意的是这个job service运行在你的主线程，这意味着你需要使用子线程，handler, 或者一个异步任务来运行耗时的操作以防止阻塞主线程。
 */
public class JobSchedulerService extends JobService {
    private MyPhoneStateReceiver mMyPhoneStateReceiver;

    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg ) {
            Toast.makeText( getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT )
                    .show();

            if (msg.what == 1) {
                Log.d(Constant.TAG, "接收任务了 ------- " + System.currentTimeMillis());
//                mJobHandler.sendEmptyMessageDelayed(1, 5000);
            }

            /**
             * 当任务执行完毕之后，你需要调用jobFinished(JobParameters params, boolean needsRescheduled)来让系统知道这个任务已经结束,
             * 系统可以将下一个任务添加到队列中. 如果你没有调用jobFinished(JobParameters params, boolean needsRescheduled),
             * 你的任务只会执行一次，而应用中的其他任务就不会被执行。
             */
//            jobFinished( (JobParameters) msg.obj, false );
            return true;
        }

    } );

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constant.TAG, "onCreate()...");
        registerPhoneStateReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constant.TAG, "onStartCommand()...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constant.TAG, "onDestroy()...");
        unregisterPhoneStateReceiver();
    }

    /**
     * 当任务开始时会执行onStartJob(JobParameters params)方法,因为这是系统用来触发已经被执行的任务.
     * 这个方法返回一个boolean值,
     * 如果返回值是false, 系统假设这个方法返回时任务已经执行完毕.
     * 如果返回值是true, 那么系统假定这个任务正要被执行, 执行任务的需要自己处理, 当任务执行完毕时你需要调用jobFinished(JobParameters params, boolean needsRescheduled)来通知系统.
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(Constant.TAG, "onStartJob()...");
        if (jobParameters != null)
            Log.d(Constant.TAG, "jobParameters = " + jobParameters.toString() + "  " + jobParameters.getJobId());
        if (mJobHandler != null) {
            mJobHandler.sendEmptyMessageDelayed(1, 5000);
        }
        return true;
    }

    /**
     * 当系统接收到一个取消请求时，系统会调用onStopJob(JobParameters params)方法取消正在等待执行的任务。
     * 很重要的一点是如果onStartJob(JobParameters params)返回false,
     * 那么系统假定在接收到一个取消请求时已经没有正在运行的任务。换句话说，onStopJob(JobParameters params)在这种情况下不会被调用。
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(Constant.TAG, "onStopJob()...");
        return false;
    }

    class MyPhoneStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent1) {
            String action = intent1.getAction();
            Log.d(Constant.TAG, "action = " + action);
            if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
                String state = intent1.getStringExtra(TelephonyManager.EXTRA_STATE);
                String incomingNumber = intent1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                    Log.d(Constant.TAG, "IDLE...   " + incomingNumber);
                } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                    Log.d(Constant.TAG, "RINGING...  " + incomingNumber);
                } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                    Log.d(Constant.TAG, "OFFHOOK...  " + incomingNumber);
                }
            }
        }
    }

    private void registerPhoneStateReceiver() {
        if (mMyPhoneStateReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PHONE_STATE");
            mMyPhoneStateReceiver = new MyPhoneStateReceiver();
            registerReceiver(mMyPhoneStateReceiver, intentFilter);
            Log.d(Constant.TAG, "registerPhoneStateReceiver()...");
        }
    }

    private void unregisterPhoneStateReceiver() {
        if (mMyPhoneStateReceiver != null)
            unregisterReceiver(mMyPhoneStateReceiver);
        mMyPhoneStateReceiver = null;
        Log.d(Constant.TAG, "unregisterPhoneStateReceiver()...");
    }
}
