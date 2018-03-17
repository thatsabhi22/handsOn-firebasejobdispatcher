package com.theleafapps.pro.handson_firebasejobdispatcher;

import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by aviator on 17/03/18.
 */

public class MyJobService extends JobService {

    private static BackgroundTask backgroundTask;

    /**
     * Called on start of job
     * @param job Firebase JobParameters
     * @return boolean If return is true, the job is run on the separate thread,
     * otherwise its on main thread
     * We return false in this method only when the job is to short which can be run on main thread
     */
    @Override
    public boolean onStartJob(final JobParameters job) {

        backgroundTask = new BackgroundTask(){
            @Override
            protected void onPostExecute(String str) {
                Toast.makeText(getApplicationContext(),
                        "Message from Background Task - "+str, Toast.LENGTH_LONG).show();

                //When the job is running on the separate thread, jobfinishied() method must be called
                //otherwise the system assumes that the job is still running in the background,
                //which means firebasejobdispatcher process always running in the background
                //and it will cause the battery to drain fast, because there is a waste process running in the background
                //When we are returning true from this method, we need to call jobfinished() method.
                jobFinished(job,false);

            }
        };
        backgroundTask.execute();

        return true;
    }

    /**
     * Called on stop of the job
     * @param job Firebase JobParameters
     * @return if the job is interrupted before completing, the system will call this method.
     * If you want to reschedule the failed job return true
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public static class BackgroundTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            return "Test of the Firebase JobService";
        }
    }
}
