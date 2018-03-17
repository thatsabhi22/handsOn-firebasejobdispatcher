package com.theleafapps.pro.handson_firebasejobdispatcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    private static final String JOB_TAG = "my_job_tag";
    Button startJob, stopJob;
    private FirebaseJobDispatcher firebaseJobDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startJob = (Button) findViewById(R.id.startJob);
        stopJob = (Button) findViewById(R.id.stopJob);

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        startJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Job job = firebaseJobDispatcher.newJobBuilder().
                        setService(MyJobService.class).
                        setLifetime(Lifetime.FOREVER).
                        setRecurring(true).
                        setTag(JOB_TAG).
                        setTrigger(Trigger.executionWindow(10, 15)).
                        setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).
                        setConstraints(Constraint.ON_ANY_NETWORK).
                        setReplaceCurrent(false).build();

                firebaseJobDispatcher.schedule(job);
                Toast.makeText(MainActivity.this, "Job Scheduled..", Toast.LENGTH_SHORT).show();
            }
        });

        stopJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseJobDispatcher.cancel(JOB_TAG);
                Toast.makeText(MainActivity.this, "Job Cancelled..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
