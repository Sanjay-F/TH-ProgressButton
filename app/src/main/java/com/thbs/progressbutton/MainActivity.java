package com.thbs.progressbutton;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.progressframework.R;

public class MainActivity extends Activity {


    static MasterLayout masterLayout;
    private DownLoadSigTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        masterLayout = (MasterLayout) findViewById(R.id.MasterLayout01);
        downloadTask = new DownLoadSigTask();
        masterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                masterLayout.starShow(); //Need to call this method for starShow and progression
                if (masterLayout.isUnStartModel()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "Starting download", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    downloadTask.execute();
                }

                if (masterLayout.isSuccessFinishModel()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "Download complete", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            }
        });

    }

    public void onResetClick(View view) {
        masterLayout.reset();
        downloadTask.stop();
        downloadTask = new DownLoadSigTask();
    }


    public void onShowFailClick(View view) {
        downloadTask.stop();
        masterLayout.showFailAnimation();
    }

    public void onShowSuccessClick(View view) {
        downloadTask.stop();
        masterLayout.showSuccessAnimation();
    }

    static class DownLoadSigTask extends AsyncTask<String, Integer, String> {


        private boolean isStop = false;

        @Override
        protected void onPreExecute() {

        }

        public void stop() {
            this.isStop = true;
        }


        @Override
        protected String doInBackground(final String... args) {
            for (int i = 0; i <= 100; i++) {
                if (isStop) {
                    break;
                }
                publishProgress(i);
                Log.e("prog", " i=" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            masterLayout.updateProgress(progress[0]);
        }


    }

}
