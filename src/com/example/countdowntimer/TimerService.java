package com.example.countdowntimer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class TimerService extends Service {
	Context mContext;
	int counter;
	Timer timer;
	public PowerManager.WakeLock wl;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		this.mContext = this;
		this.counter = intent.getIntExtra("counter", 0);
		if (this.counter != 0) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK + PowerManager.ON_AFTER_RELEASE, "My Tag");
			this.wl.acquire();
			this.startTimer();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.timer.cancel();
		if (this.wl.isHeld()) {
			this.wl.release();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void startTimer() {
		if (this.timer != null) {
			this.timer.cancel();
		}
		this.timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if (counter == -1) {
							timer.cancel();
							if (wl.isHeld()) {
								wl.release();
							}
							showAlarm();
						}
						else {
							CountdownTimerActivity.countdown(counter);
							counter = counter - 1;
						}
					}
				});
			}
		}, 0, 1000);
	}

	void showAlarm() {
		Intent intent = new Intent(this.mContext, TimerService.class);
		this.mContext.stopService(intent);
		intent = new Intent(this.mContext, AlarmDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.mContext.startActivity(intent);
	}

}
