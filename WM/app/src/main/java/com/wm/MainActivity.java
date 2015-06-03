package com.wm;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wm.lib.api.AsyncExecutor;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String TAG = "bryant";

    @Bean
    AsyncExecutor mAsyncExecutor;

    @AfterViews
    void init() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Click(R.id.btn)
    void onBtnClick() {
        EventBus.getDefault().post(new MessageEvent("异步任务开始执行..."));
        mAsyncExecutor.execute(new AsyncExecutor.AsyncCallBack<String>() {
            @Override
            public void onPreExecute() {
                log("执行前...");
            }

            @Override
            public void onSuccess(String result) {
                log("执行成功！" + result);
            }

            @Override
            public void onFail(Exception e) {
                log("执行失败！" + e.getMessage());
            }

            @Override
            public String onExecute() {
                log("执行中...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "how are you！！";
            }
        });
    }

    private void log(String message) {
        Log.d(TAG, message);
    }

    // Called in Android UI's main thread
    public void onEventMainThread(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    public class MessageEvent {

        public final String message;

        public MessageEvent(String message) {
            this.message = message;
        }

    }

}
