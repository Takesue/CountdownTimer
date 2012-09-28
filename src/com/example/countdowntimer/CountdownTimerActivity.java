package com.example.countdowntimer;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CountdownTimerActivity extends Activity {
	
	// TimerService側でcounter()メソッドを実行する必要があり、counterはクラスメソッドとなっている。
	// クラスメソッド内で使用されている以下メンバクラスはクラス変数として定義する必要あり。
	static TextView tv;
	static SeekBar sb;
	static Context mContext;
	static int timeLeft = 0;
	static Button btnStart, btnStop;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.count_down_timer);
		
		CountdownTimerActivity.mContext = this;
		CountdownTimerActivity.tv = (TextView) this.findViewById(R.id.textView1);
		CountdownTimerActivity.btnStart = (Button) this.findViewById(R.id.buttonStart);
		CountdownTimerActivity.btnStop = (Button) this.findViewById(R.id.buttonStop);
		CountdownTimerActivity.sb = (SeekBar)this.findViewById(R.id.seekBar1);
		CountdownTimerActivity.sb.setBackgroundDrawable(this.drawScale());
		this.setListneners();
	}

	private BitmapDrawable drawScale() {
		
		Paint paint;
		Path path;
		Canvas canvas;
		Bitmap bitmap;
		
		paint = new Paint();
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.STROKE);
		bitmap = Bitmap.createBitmap(241, 30, Bitmap.Config.ARGB_8888);
		path = new Path();
		canvas = new Canvas(bitmap);
		
		for(int i = 0; i < 17; i++) {
			path.reset();
			if(i == 5|| i == 10 || i == 15) {
				paint.setColor(Color.WHITE);
			}
			else {
				paint.setColor(Color.GRAY);
			}
			
			path.moveTo(i*16, 5);
			path.quadTo(i*16, 5, i*16, 15);
			canvas.drawPath(path, paint);
			
		}
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		
		return bd;
	}

	void setListneners() {
		
		CountdownTimerActivity.sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				CountdownTimerActivity.timeLeft = progress*60;
				if (fromUser) {
					CountdownTimerActivity.showTime(progress*60);
				}
				if(fromUser && (progress > 0)) {
					CountdownTimerActivity.btnStart.setEnabled(true);
				}
				else {
					CountdownTimerActivity.btnStart.setEnabled(false);
				}
				if (progress == 0) {
					CountdownTimerActivity.btnStop.setEnabled(false);
				}
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
		});
		
		CountdownTimerActivity.btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CountdownTimerActivity.mContext, TimerService.class);
				intent.putExtra("counter", CountdownTimerActivity.timeLeft);
				startService(intent);
				CountdownTimerActivity.btnStart.setEnabled(false);
				CountdownTimerActivity.btnStop.setEnabled(true);
				CountdownTimerActivity.sb.setEnabled(false);
			}
		});
		CountdownTimerActivity.btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CountdownTimerActivity.mContext, TimerService.class);
				CountdownTimerActivity.mContext.stopService(i);
				CountdownTimerActivity.btnStop.setEnabled(false);
				CountdownTimerActivity.btnStart.setEnabled(true);
				CountdownTimerActivity.sb.setEnabled(true);
			}
		});
		
		((Button) this.findViewById(R.id.buttonSettings)).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(CountdownTimerActivity.this, Preferences.class);
						startActivity(intent);
					}
		});

	}

	public static void showTime(int timeSeconds) {
		SimpleDateFormat form = new SimpleDateFormat("mm:ss");
		CountdownTimerActivity.tv.setText(form.format(timeSeconds*1000));
	}
	
	public static void countdown(int counter) {
		CountdownTimerActivity.showTime(counter);
		CountdownTimerActivity.timeLeft = counter;
		if (counter%60 == 0) {
			CountdownTimerActivity.sb.setProgress(counter/60);
		}
		else {
			CountdownTimerActivity.sb.setProgress(counter/60 + 1);
		}
		
		if (counter != 0) {
			CountdownTimerActivity.btnStop.setEnabled(true);
			CountdownTimerActivity.btnStart.setEnabled(false);
			CountdownTimerActivity.sb.setEnabled(false);
		}
		else {
			CountdownTimerActivity.btnStop.setEnabled(false);
			CountdownTimerActivity.btnStart.setEnabled(false);
			CountdownTimerActivity.sb.setEnabled(true);
		}
	}
}
